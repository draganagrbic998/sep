import { RoleAuth } from "./auth";
import { PaymentMethod } from "./payment-method";
import { StandardModel } from "./standard-model";

export interface User extends StandardModel{
    email: string
    password: string
    role: RoleAuth
    methods: PaymentMethod[]
    webshop: string
}