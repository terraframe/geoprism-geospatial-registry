import { Component, OnInit, AfterViewInit, ViewChild, ElementRef, Input, Output, EventEmitter } from '@angular/core';
import {
  trigger,
  state,
  style,
  animate,
  transition
} from '@angular/animations'
import {NgControl, Validators, FormBuilder} from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { ButtonsModule } from 'ngx-bootstrap/buttons';

import { ContextMenuService, ContextMenuComponent } from 'ngx-contextmenu';

import { TreeEntity, HierarchyType, GeoObjectType, Attribute, AttributeTerm, Term, ManageAttributeState } from '../hierarchy';
import { TreeNode, TreeComponent, TreeDropDirective } from 'angular-tree-component';
import { HierarchyService } from '../../../service/hierarchy.service';

import { AttributeInputComponent} from '../form-inputs/attribute-input.component';

import { GeoObjectAttributeCodeValidator } from '../../../factory/form-validation.factory';


@Component( {
    selector: 'edit-attribute-modal-content',
    templateUrl: './edit-attribute-modal-content.component.html',
    styleUrls: [],
    animations: [
        trigger('openClose', 
            [
                transition(
                ':enter', [
                style({ 'opacity': 0}),
                animate('500ms', style({ 'opacity': 1}))
                ]
            ),
            transition(
                ':leave', [
                style({ 'opacity': 1}),
                animate('0ms', style({'opacity': 0})),
                
                ]
            )]
      )
    ]
} )
export class EditAttributeModalContentComponent implements OnInit {

    @Input() geoObjectType: GeoObjectType;
    @Input() attribute: Attribute;
    @Input() modalState: ManageAttributeState;
    @Output() modalStateChange = new EventEmitter<ManageAttributeState>();
    message: string = null;

    @ViewChild(AttributeInputComponent) attributeInputComponent:AttributeInputComponent;

    constructor( private hierarchyService: HierarchyService, public bsModalRef: BsModalRef, private contextMenuService: ContextMenuService ) {
      if(this.modalState){
        console.log(this.modalState.attribute)
      }
    }

    ngOnInit(): void {
        if(this.modalState){
            console.log(this.modalState.attribute)
        }
    }

    ngAfterViewInit() {
   
    }

    ngOnDestroy(){
    }

    handleOnSubmit(): void {
        
        this.hierarchyService.updateAttributeType( this.geoObjectType.code, this.attribute ).then( data => {
            
            // TODO: update attributes
            this.modalStateChange.emit({"state":"MANAGE-ATTRIBUTES", "attribute":""});
        } ).catch(( err: any ) => {
            this.error( err.json() );
        } );
    }

    isFormValid(): boolean {
        
        let isAttrValid: boolean = this.attributeInputComponent.isValid();
        
        if(isAttrValid){
            return true;
        }

        return false;
    }

    cancel(): void {
        this.modalStateChange.emit({"state":"MANAGE-ATTRIBUTES", "attribute":""});
    }

    error( err: any ): void {
        // Handle error
        if ( err !== null ) {
            this.message = ( err.localizedMessage || err.message );
            
            console.log(this.message);
        }
    }

}