import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { StandardModel } from 'src/app/models/standard-model';
import { FormService } from 'src/app/services/form.service';
import { FormConfig, FormStyle } from 'src/app/utils/form';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss']
})
export class FormComponent implements OnInit {

  constructor(
    private formService: FormService
  ) { }

  @Input() config: {
    title: string;
    formConfig: FormConfig;
    pending: boolean;
    style: FormStyle;
    save: (value: any) => void;
    readFunction: () => Observable<StandardModel>;
  }

  form: FormGroup;

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
    if (this.config.readFunction) {
      const value = await this.config.readFunction().toPromise();
      this.form.reset(value);
    }
  }

  type(control: string) {
    return this.config.formConfig[control].type || 'text'
  }

  options(control: string) {
    return this.config.formConfig[control].options
  }

  validation(control: string) {
    return this.config.formConfig[control].validation
  }

  handleSubmit() {
    if (this.form.invalid) {
      this.form.markAsTouched();
      return;
    }
    this.config.save(this.form.value);
  }

  capitalize(text: string) {
    text = text.replace(/([a-z])([A-Z])/g, '$1 $2');
    return text[0].toUpperCase() + text.substr(1).toLowerCase();
  }

  updateFile(control: string, file: Blob) {
    this.form.get(control).setValue(file);
  }

}
