import { RoleAuth } from "./auth";
import { StandardModel } from "./standard-model";

export interface User extends StandardModel {
    email: string;
    password: string;
    role: RoleAuth;
    apiKey: string;
}