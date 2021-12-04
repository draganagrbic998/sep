import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';
import { SNACKBAR_CLOSE_BUTTON, SNACKBAR_ERROR_CONFIG, SNACKBAR_ERROR_TEXT, SNACKBAR_SUCCESS_CONFIG, SNACKBAR_SUCCESS_TEXT } from 'src/app/utils/popup';

@Component({
  selector: 'app-delete-confirmation',
  templateUrl: './delete-confirmation.component.html',
  styleUrls: ['./delete-confirmation.component.scss']
})
export class DeleteConfirmationComponent {

  constructor(
    @Inject(MAT_DIALOG_DATA) private deleteFunction: () => Observable<void>,
    private snackBar: MatSnackBar,
    public dialogRef: MatDialogRef<DeleteConfirmationComponent>
  ) { }

  deletePending = false;

  async confirm() {
    this.deletePending = true;
    try {
      await this.deleteFunction().toPromise();
      this.deletePending = false;
      this.dialogRef.close(true);
      this.snackBar.open(SNACKBAR_SUCCESS_TEXT, SNACKBAR_CLOSE_BUTTON, SNACKBAR_SUCCESS_CONFIG);

    } catch {
      this.deletePending = false;
      this.snackBar.open(SNACKBAR_ERROR_TEXT, SNACKBAR_CLOSE_BUTTON, SNACKBAR_ERROR_CONFIG);

    }
  }

}
