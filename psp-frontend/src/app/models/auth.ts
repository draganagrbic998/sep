export interface Auth {
    email: string;
    password: string;
    roles: Role[];
    token: string;
}

export enum Role {
    PSP_ADMIN = 'psp-admin',
    WS_ADMIN = 'ws-admin',
    MERCHANT = 'merchant'
}
