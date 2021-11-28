import { StandardModel } from "./standard-model";

export interface Page<T extends StandardModel>{
    content: T[];
    number: number;
    size: number;
}