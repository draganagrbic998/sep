import { RoleAuth } from "./auth";
import { StandardModel } from "./standard-model";

export interface User extends StandardModel{
    role: RoleAuth;
    email: string;
    password: string;
    apiKey: string;
}