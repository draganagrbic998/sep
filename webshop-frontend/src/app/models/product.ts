import { StandardModel } from "./standard-model";

export interface ProductUpload extends StandardModel {
    name: string;
    description: string;
    price: string;
    category: string;
    currency: string;
    image: Blob;
}

export interface Product extends StandardModel {
    id: number;
    name: string;
    description: string;
    price: string;
    category: string;
    currency: string;
    imageLocation: string;
}