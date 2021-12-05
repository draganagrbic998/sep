import { Observable } from 'rxjs'

export interface FormConfig {
  [control: string]: {
    type?: 'text' | 'password' | 'select' | 'multi-select'
    validation?: 'none' | 'required' | 'price'
    options?: Observable<unknown[]>
    optionValue?: string
    optionKey?: string
    hidding?: {
      field: string
      values: string[]
    }
    hide?: boolean
  }
}

export interface FormStyle {
  width?: string
  'margin-top'?: string
}
