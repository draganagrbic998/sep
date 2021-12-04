import { StandardModel } from "./standard-model";

export interface User extends StandardModel{
    email: string
    password: string
    role: string
}