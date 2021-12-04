export interface Auth {
    email: string;
    password: string;
    role: RoleAuth;
    token: string;
}

export enum RoleAuth {
    WS_ADMIN = 'ws-admin',
    MERCHANT = 'merchant'
}
