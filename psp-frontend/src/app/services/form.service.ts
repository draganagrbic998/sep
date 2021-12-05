import { Injectable } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { FormConfig } from '../utils/form';

@Injectable({
  providedIn: 'root'
})
export class FormService {

  constructor(
    private builder: FormBuilder
  ) { }

  build(config: FormConfig) {
    const data = {}
    for (const control in config) {
      data[control] = [config[control].type !== 'multi-select' ? '' : [], this.buildValidation(config[control].validation)]
    }
    return this.builder.group(data);
  }

  private buildValidation(config: 'none' | 'required') {
    if (config === 'required') {
      return [Validators.required, Validators.pattern(new RegExp('\\S'))]
    }
    return undefined;
  }

}
