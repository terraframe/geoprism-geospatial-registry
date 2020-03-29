package net.geoprism.registry.task;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.LocalizationFacade;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.localization.LocalizedValueStore;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.Roles;

public class Task extends TaskBase
{
  private static final long serialVersionUID = 508070126;
  
  public Task()
  {
    super();
  }
  
  public static Task createNewTask(Collection<Roles> roles, LocalizedValueStore lvs, Map<String, LocalizedValue> values)
  {
    Task task = new Task();
    task.setTemplate(lvs);
    
    processLocale(lvs, values, task, MdAttributeLocalInfo.DEFAULT_LOCALE);
    
    List<Locale> locales = LocalizationFacade.getInstalledLocales();
    for (Locale locale : locales)
    {
      processLocale(lvs, values, task, locale.toString());
    }
    
    task.apply();
    
    for (Roles role : roles)
    {
      TaskHasRole hasRole = new TaskHasRole(task, role);
      hasRole.apply();
    }
    
    return task;
  }

  private static void processLocale(LocalizedValueStore lvs, Map<String, LocalizedValue> values, Task task, String locale)
  {
    String template = lvs.getStoreValue().getValue(locale);
    StringBuilder sb = new StringBuilder(template);
    
    int offset = 0;
    String pattern = "\\{.*?\\}";
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(template);
    while (m.find())
    {
      String g = m.group();
      String token = g.substring(1, g.length()-1);
      
      if (values.containsKey(token))
      {
        LocalizedValue value = values.get(token);
        
        String replacement = value.getValue(locale);
        
        sb.replace(m.start() + offset, m.end() + offset, replacement);
        
        offset += replacement.length() - (m.end() - m.start());
      }
    }
    
    task.getMessage().setValue(locale, sb.toString());
  }
  
  @Override
  public void delete()
  {
    TaskHasRoleQuery thq = new TaskHasRoleQuery(new QueryFactory());
    thq.WHERE(thq.getParent().EQ(this.getOid()));
    
    OIterator<? extends TaskHasRole> it = thq.getIterator();
    try
    {
      while (it.hasNext())
      {
        it.next().delete();
      }
    }
    finally
    {
      it.close();
    }
    
    super.delete();
  }
  
  // This code was written but we ended up using the method in TaskService instead. If you find you need this code
  // in future, feel free to comment it back in.
//  public static TaskHasRoleQuery getTasksForCurrentUser(int pageSize, int pageNumber, String sortAttr)
//  {
//    TaskHasRoleQuery query = new TaskHasRoleQuery(new QueryFactory());
//    
//    Map<String, String> roles = Session.getCurrentSession().getUserRoles();
//    
//    Condition cond = null;
//    
//    for (String roleName : roles.keySet())
//    {
//      Roles role = Roles.findRoleByName(roleName);
//      
//      if (cond == null)
//      {
//        cond = query.getChild().EQ(role);
//      }
//      else
//      {
//        cond = cond.OR(query.getChild().EQ(role));
//      }
//    }
//    
//    query.WHERE(cond);
//    
//    query.ORDER_BY(query.get(sortAttr), SortOrder.ASC);
//    
//    query.restrictRows(pageSize, pageNumber);
//    
//    return query;
//  }
}
