import { Component, Input, OnInit } from '@angular/core'
import { FormGroup } from '@angular/forms'
import { MatSnackBar } from '@angular/material/snack-bar'
import { ActivatedRoute, Router } from '@angular/router'
import { StandardModel } from 'src/app/models/standard-model'
import { FormService } from 'src/app/services/form.service'
import { StandardRestService } from 'src/app/services/standard-rest.service'
import { FormConfig, FormStyle } from 'src/app/utils/form'
import {
  SNACKBAR_CLOSE_BUTTON,
  SNACKBAR_ERROR_CONFIG,
  SNACKBAR_ERROR_TEXT,
  SNACKBAR_SUCCESS_CONFIG,
  SNACKBAR_SUCCESS_TEXT
} from 'src/app/utils/popup'

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

  title: string
  form: FormGroup
  pending = false

  @Input() config: {
    formConfig: FormConfig
    style: FormStyle
    service?: StandardRestService<StandardModel>
    listRoute?: string
    entity?: string
    title?: string
    pending?: boolean
    save?: (value: unknown) => void
  }

  get itemId() {
    return +this.route.snapshot.params.id
  }

  get isPending() {
    if (this.config.pending !== undefined) {
      return this.config.pending
    }
    return this.pending;
  }

  get controls() {
    let temp = Object.keys(this.config.formConfig)
    for (const control of temp) {
      const hidding = this.config.formConfig[control].hidding
      if (hidding?.values.includes(this.form.get(hidding.field).value)) {
        temp = temp.filter(item => item !== control)
      }
    }
    return temp
  }

  ngOnInit() {
    this.form = this.formService.build(this.config.formConfig)
    this.initForm()
  }

  private async initForm() {
    if (this.config.title) {
      return
    }
    if (this.itemId) {
      this.title = `Edit ${this.config.entity}`
      this.form.reset(await this.config.service.readOne(this.itemId).toPromise())
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

  optionKey(control: string) {
    return this.config.formConfig[control].optionKey
  }

  optionValue(control: string) {
    return this.config.formConfig[control].optionValue
  }

  handleSubmit() {
    for (const control of Object.keys(this.config.formConfig)) {
      if (this.form.get(control).invalid && this.controls.includes(control)) {
        this.form.markAsTouched()
        return
      }
    }

    if (this.config.save) {
      this.config.save(this.form.value)
    } else {
      this.save(this.form.value)
    }
  }

  compareOptions(item1: any, item2: any) {
    if (typeof item1 === 'object' && typeof item2 === 'object') {
      if ('name' in item1 && 'name' in item2) {
        return item1['name'] === item2['name']
      }
      if ('id' in item1 && 'id' in item2) {
        return item1['id'] === item2['id']
      }
    }
    return item1 === item2
  }

  capitalize(text: string) {
    text = text.replace(/([a-z])([A-Z])/g, '$1 $2')
    return text[0].toUpperCase() + text.substr(1).toLowerCase()
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
