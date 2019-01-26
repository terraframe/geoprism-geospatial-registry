import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';

import { GeoObjectType } from '../../hierarchy/hierarchy';
import { ShapefileConfiguration } from '../shapefile';

import { ShapefileService } from '../../../service/shapefile.service';

@Component( {
    selector: 'shapefile-modal',
    templateUrl: './shapefile-modal.component.html',
    styleUrls: []
} )
export class ShapefileModalComponent implements OnInit {

    configuration: ShapefileConfiguration;

    message: string = null;

    constructor( private service: ShapefileService, public bsModalRef: BsModalRef ) {
    }

    ngOnInit(): void {
    }

    onSubmit(): void {
        this.service.importShapefile( this.configuration ).then( response => {
            this.bsModalRef.hide()
        } ).catch(( err: any ) => {
            this.error( err.json() );
        } );

    }

    onCancel(): void {
        this.service.cancelImport( this.configuration ).then( response => {
            this.bsModalRef.hide()
        } ).catch(( err: any ) => {
            this.error( err.json() );
        } );
    }

    error( err: any ): void {
        // Handle error
        if ( err !== null ) {
            this.message = ( err.localizedMessage || err.message );
        }
    }
}
