export interface Auth {
    email: string;
    password: string;
    roles: Role[];
    token: string;
}

export enum Role {
    ADMIN = 'admin',
    SELLER = 'seller',
    CUSTOMER = 'customer'
}
