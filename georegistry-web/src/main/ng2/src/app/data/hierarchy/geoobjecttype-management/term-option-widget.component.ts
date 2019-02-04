import { Component, OnInit, Input, Output, EventEmitter, ChangeDetectorRef } from '@angular/core';
import {
  trigger,
  state,
  style,
  animate,
  transition
} from '@angular/animations'
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';

import { GeoObjectType, AttributeTerm, GeoObjectTypeModalStates, ManageGeoObjectTypeModalState} from '../hierarchy';
import { HierarchyService } from '../../../service/hierarchy.service';
import { GeoObjectTypeManagementService } from '../../../service/geoobjecttype-management.service'

import { GeoObjectAttributeCodeValidator } from '../../../factory/form-validation.factory';



@Component( {
    selector: 'term-option-widget',
    templateUrl: './term-option-widget.component.html',
    styleUrls: ['./term-option-widget.css'],
    animations: [
        trigger('toggleInputs', [
            state('none, void', 
                style({ 'opacity': 0})
              ),
              state('show', 
                style({ 'opacity': 1})
              ),
              transition('none => show', animate('300ms')),
              transition('show => none', animate('100ms'))
        ]),
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
export class TermOptionWidgetComponent implements OnInit {

    @Input() geoObjectType: GeoObjectType;
    @Input() attribute: AttributeTerm;
    @Output() attributeChange = new EventEmitter<AttributeTerm>();
    // @Input() modalState: ManageGeoObjectTypeModalState;
    // @Output() modalStateChange = new EventEmitter<ManageGeoObjectTypeModalState>();
    message: string = null;
    termOptionCode: string = "";
    termOptionLabel: string = "";
    state: string = 'none';
    // enableTermOptionForm = false;
    modalState: ManageGeoObjectTypeModalState = {"state":GeoObjectTypeModalStates.editTermOption, "attribute":this.attribute};

    constructor( private hierarchyService: HierarchyService, public bsModalRef: BsModalRef, private cdr: ChangeDetectorRef, private geoObjectTypeManagementService: GeoObjectTypeManagementService ) {
    }

    ngOnInit(): void {

    }

    ngAfterViewInit() {
        this.state = 'show';
        this.cdr.detectChanges();
    }

    ngOnDestroy(){
    
    }

    handleOnSubmit(): void {
        
    }

    animate(): void {
        this.state = "none";
    }

    onAnimationDone(event: AnimationEvent): void {
        this.state = "show";
    }

    isValid(): boolean {
        if(this.termOptionCode && this.termOptionCode.length > 0 && this.termOptionLabel && this.termOptionLabel.length > 0){
            
            // If code has a space
            if(this.termOptionCode.indexOf(" ") !== -1){
                return false;
            }

            // If label is only spaces
            if(this.termOptionLabel.replace(/\s/g, '').length === 0) {
                return false
            }

            return true;
        }
        else if(this.termOptionCode && this.termOptionCode.indexOf(" ") !== -1){
            return false;
        }
            
        return false
    }

    onModalStateChange(state: any): void {
        console.log("state change in term option input")
        this.modalState = state;
    }

    openAddTermOptionForm(): void {
        // this.modalState = {"state":GeoObjectTypeModalStates.editTermOption, "attribute":this.attribute};
        // this.modalStateChange.emit(this.modalState);
        this.geoObjectTypeManagementService.setModalState({"state":GeoObjectTypeModalStates.editTermOption, "attribute":this.attribute})

    }
    
    error( err: any ): void {
        // Handle error
        if ( err !== null ) {
            this.message = ( err.localizedMessage || err.message );
            
            console.log(this.message);
        }
    }

}