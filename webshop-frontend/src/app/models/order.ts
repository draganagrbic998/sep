import { Product } from "./product";
import { User } from "./user";

export interface Order {
    status: string;
    id: number;
    user: User;
    product: Product;
    quantity: number;
    date: Date;
    pspId: number;
}