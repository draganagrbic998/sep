import { Observable } from "rxjs";

export interface FormConfig {
    [control: string]: {
        type?: 'text' | 'password' | 'file' | 'select',
        validation?: 'none' | 'required' | 'price',
        options?: Observable<string[]>
    }
}

export interface FormStyle {
    width?: string;
    'margin-top'?: string;
}
