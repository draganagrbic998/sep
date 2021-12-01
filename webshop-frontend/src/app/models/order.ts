import { CartItem } from "./cart-item";

export interface Order {
    id: number;
    date: Date;
    items: CartItem[];
}