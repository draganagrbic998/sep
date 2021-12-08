import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { StandardModel } from 'src/app/models/standard-model';
import { FormService } from 'src/app/services/form.service';
import { StandardRestService } from 'src/app/services/standard-rest.service';
import { FormConfig, FormStyle } from 'src/app/utils/form';
import { SNACKBAR_CLOSE_BUTTON, SNACKBAR_ERROR_CONFIG, SNACKBAR_ERROR_TEXT, SNACKBAR_SUCCESS_CONFIG, SNACKBAR_SUCCESS_TEXT } from 'src/app/utils/popup';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss']
})
export class FormComponent implements OnInit {

  constructor(
    private formService: FormService,
    private router: Router,
    private route: ActivatedRoute,
    private snackbar: MatSnackBar,
  ) { }

  @Input() config: {
    formConfig: FormConfig;
    style: FormStyle;
    service?: StandardRestService<StandardModel>
    title?: string;
    entity?: string
    listRoute?: string
    save?: (value: unknown) => void
  }

  title: string;
  form: FormGroup;
  pending = false

  get itemId() {
    return +this.route.snapshot.params.id
  }

  get controls() {
    return Object.keys(this.config.formConfig).filter(control => this.config.formConfig[control].type !== 'file')
  }

  get fileControls() {
    return Object.keys(this.config.formConfig).filter(control => this.config.formConfig[control].type === 'file')
  }

  ngOnInit() {
    this.form = this.formService.build(this.config.formConfig);
    this.initForm()
  }

  private async initForm() {
    if (this.config.title) {
      return
    }
    if (this.itemId) {
      this.title = `Edit ${this.config.entity}`
      const value = await this.config.service.readOne(this.itemId).toPromise()
      this.form.reset(value)
    } else {
      this.title = `Create ${this.config.entity}`
    }
  }

  type(control: string) {
    return this.config.formConfig[control].type || 'text'
  }

  validation(control: string) {
    return this.config.formConfig[control].validation
  }

  options(control: string) {
    return this.config.formConfig[control].options
  }

  handleSubmit() {
    if (this.form.invalid) {
      this.form.markAsTouched();
      return;
    }
    if (this.config.save) {
      this.config.save(this.form.value);

    }
    else {
      this.save(this.form.value);
    }
  }

  capitalize(text: string) {
    text = text.replace(/([a-z])([A-Z])/g, '$1 $2');
    return text[0].toUpperCase() + text.substr(1).toLowerCase();
  }

  updateFile(control: string, file: Blob) {
    this.form.get(control).setValue(file);
  }

  async save(item: StandardModel) {
    if (this.itemId) {
      item.id = this.itemId
    }
    this.pending = true

    try {
      await this.config.service.save(item).toPromise()
      this.pending = false
      this.snackbar.open(
        SNACKBAR_SUCCESS_TEXT,
        SNACKBAR_CLOSE_BUTTON,
        SNACKBAR_SUCCESS_CONFIG
      )
      this.router.navigate([this.config.listRoute])
    } catch {
      this.pending = false
      this.snackbar.open(
        SNACKBAR_ERROR_TEXT,
        SNACKBAR_CLOSE_BUTTON,
        SNACKBAR_ERROR_CONFIG
      )
    }
  }

}
