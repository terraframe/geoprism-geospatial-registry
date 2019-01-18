/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.georegistry.excel;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.geoprism.ExceptionUtil;
import net.geoprism.data.etl.ColumnType;
import net.geoprism.data.etl.excel.ExcelDataFormatter;
import net.geoprism.data.etl.excel.ExcelFormulaException;
import net.geoprism.data.etl.excel.ExcelObjectException;
import net.geoprism.data.etl.excel.ExcelValueException;
import net.geoprism.data.etl.excel.InvalidHeaderRowException;
import net.geoprism.data.etl.excel.SheetHandler;
import net.geoprism.georegistry.io.GeoObjectConfiguration;
import net.geoprism.localization.LocalizationFacade;

public class GeoObjectContentHandler implements SheetHandler
{
  private static Logger        logger = LoggerFactory.getLogger(GeoObjectContentHandler.class);

  /**
   * Handler which handles the view object once they have been created.
   */
  private GeoObjectConverter   converter;

  /**
   * Current sheet name
   */
  private String               sheetName;

  /**
   * Column index-Column Name Map for the current sheet
   */
  private Map<Integer, String> map;

  /**
   * Current row number
   */
  private int                  rowNum;

  /**
   * Current error row number
   */
  private int                  errorNum;

  /**
   * Current view
   */
  private Map<String, Object>  row;

  /**
   * Decimal format used to remove trailing 0s from the long values
   */
  private DecimalFormat        nFormat;

  /**
   * Format used for parsing and formatting dateTime fields
   */
  private DateFormat           dateTimeFormat;

  /**
   * Format used for parsing and formatting dateTime fields
   */
  private DateFormat           dateFormat;

  /**
   * Workbook containing error rows
   */
  private SXSSFWorkbook        workbook;

  /**
   * Date style format
   */
  private CellStyle            style;

  boolean                      isFirstSheet;

  public GeoObjectContentHandler(GeoObjectConfiguration configuration)
  {
    this.isFirstSheet = true;

    this.converter = new GeoObjectConverter(configuration);

    this.map = new HashMap<Integer, String>();
    this.nFormat = new DecimalFormat("###.#");

    this.dateTimeFormat = new SimpleDateFormat(ExcelDataFormatter.DATE_TIME_FORMAT);
    this.dateFormat = new SimpleDateFormat(ExcelDataFormatter.DATE_FORMAT);
  }

  @Override
  public void startSheet(String sheetName)
  {
    this.sheetName = sheetName;
    this.errorNum = 1;
  }

  @Override
  public void endSheet()
  {
  }

  @Override
  public void startRow(int rowNum)
  {
    if (this.isFirstSheet)
    {
      this.rowNum = rowNum;
      this.row = new HashMap<String, Object>();
    }
  }

  @Override
  public void endRow()
  {
    if (this.isFirstSheet)
    {
      try
      {
        if (this.rowNum != 0)
        {
          this.converter.create(new MapFeatureRow(this.row));

          this.row = new HashMap<String, Object>();
        }
      }
      catch (Exception e)
      {
        // Wrap all exceptions with information about the cell and row
        ExcelObjectException exception = new ExcelObjectException(e);
        exception.setRow(new Long(this.rowNum));
        exception.setMsg(ExceptionUtil.getLocalizedException(e));

        throw exception;
      }
    }
  }

  private String setColumnName(String cellReference, String columnName)
  {
    CellReference reference = new CellReference(cellReference);
    Integer column = new Integer(reference.getCol());

    return this.map.put(column, columnName);
  }

  private String getColumnName(String cellReference)
  {
    CellReference reference = new CellReference(cellReference);
    Integer column = new Integer(reference.getCol());

    return this.map.get(column);
  }

  @Override
  public void cell(String cellReference, String contentValue, String formattedValue, ColumnType cellType)
  {
    if (this.isFirstSheet)
    {

      try
      {
        if (cellType.equals(ColumnType.FORMULA))
        {
          throw new ExcelFormulaException();
        }

        if (this.rowNum == 0)
        {
          if (! ( cellType.equals(ColumnType.TEXT) || cellType.equals(ColumnType.INLINE_STRING) ))
          {
            throw new InvalidHeaderRowException();
          }

          this.setColumnName(cellReference, formattedValue);

          // Store the original value in a temp map in case there is an error
          String label = LocalizationFacade.getFromBundles("dataUploader.causeOfFailure");

          if (!contentValue.equals(label))
          {
            this.row.put(cellReference, contentValue);
          }
        }
        else if (this.row != null)
        {
          String columnName = this.getColumnName(cellReference);

          this.row.put(columnName, formattedValue);
        }
      }
      catch (Exception e)
      {
        logger.error("An error occurred while importing cell [" + cellReference + "].", e);

        // Wrap all exceptions with information about the cell and row
        ExcelValueException exception = new ExcelValueException();
        exception.setCell(cellReference);
        exception.setMsg(ExceptionUtil.getLocalizedException(e));

        throw exception;
      }
    }
  }

  @Override
  public void headerFooter(String text, boolean isHeader, String tagName)
  {
  }

  public int getTotalRows()
  {
    return rowNum;
  }

  public int getNumberOfErrors()
  {
    return ( this.errorNum - 1 );
  }

  @Override
  public void setDatasetProperty(String dataset)
  {
    // Do nothing
  }
}