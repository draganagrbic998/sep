import { StandardModel } from "./standard-model";

export interface ProductUpload extends StandardModel {
    name: string;
    description: string;
    category: string;
    price: string;
    currency: string;
    image: Blob;
}

export interface Product extends StandardModel {
    name: string;
    description: string;
    category: string;
    price: number;
    currency: string;
    imageLocation: string;
}
