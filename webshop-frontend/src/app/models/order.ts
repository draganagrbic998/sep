import { Product } from "./product";
import { User } from "./user";

export interface Order {
    id: number;
    user: User;
    product: Product;
    quantity: number;
    date: Date;
    pspId: number;
}