import { Observable } from 'rxjs'

export interface FormConfig {
  [control: string]: {
    type?: 'text' | 'password' | 'file' | 'select' | 'multi-select'
    validation?: 'none' | 'required' | 'price'
    options?: Observable<unknown[]>
    optionValue?: string
    optionKey?: string
    hidding?: {
      field: string
      value: string
    }
  }
}

export interface FormStyle {
  width?: string
  'margin-top'?: string
}
