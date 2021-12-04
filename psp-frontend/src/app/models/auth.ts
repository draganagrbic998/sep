export interface Auth {
    email: string;
    password: string;
    role: RoleAuth;
    token: string;
}

export enum RoleAuth {
    PSP_ADMIN = 'psp-admin',
    WS_ADMIN = 'ws-admin',
    MERCHANT = 'merchant'
}
