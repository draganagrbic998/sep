export interface Auth {
    email: string;
    password: string;
    role: Role[];
    token: string;
}

export enum Role {
    ADMIN = 'admin',
    SELLER = 'seller',
    CUSTOMER = 'customer'
}
