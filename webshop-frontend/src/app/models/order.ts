import { Product } from "./product";

export interface Order {
    id: number;
    product: Product;
    quantity: number;
    date: Date;
}