export default class AuthResponse {
    authToken: string;
    userId: string;
    role: string;
    name: string;
    email: string;
    pictureUrl: string;

    constructor(authToken: string, userId: string, role: string, name: string, email: string, pictureUrl: string) {
        this.authToken = authToken;
        this.userId = userId;
        this.role = role;
        this.name = name;
        this.email = email;
        this.pictureUrl = pictureUrl;
    }
}
