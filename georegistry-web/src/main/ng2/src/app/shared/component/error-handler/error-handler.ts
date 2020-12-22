
import { HttpErrorResponse } from '@angular/common/http';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { BsModalService } from 'ngx-bootstrap/modal';
import { ErrorModalComponent } from '@shared/component';

export class ErrorHandler {
    static getMessageFromError(err: any): string {
    
      var unspecified = "An unspecified error has occurred. Please contact your technical support team.";
    
      if (err == null)
      {
        return unspecified;
      }
      else
      {
        console.log("An error has occurred: ", err);
      }
    
      if (err.error != null)
      {
        var msg = err.error.localizedMessage || err.error.message;
        
        if (msg == null)
        {
          return unspecified;
        }
        else if (msg.includes("##tferrormsg##"))
        {
          var split = msg.split("##tferrormsg##");
          return split[2];
        }
        else
        {
          return msg;
        }
      }
     
      return unspecified;
    }
    
    static showErrorAsDialog(err: any, modalService: BsModalService): BsModalRef {
      
      if (err instanceof HttpErrorResponse && err.status == 401)
      {
        return null;
      }
      
      let bsModalRef = modalService.show(ErrorModalComponent, { backdrop: true });
      
      bsModalRef.content.message = ErrorHandler.getMessageFromError(err);
      
      return bsModalRef;
      
    }
}
