import { Product } from "./product";
import { User } from "./user";

export interface Order {
    id: number;
    pspId: number;
    product: Product;
    quantity: number;
    date: Date;
    user: User;
}