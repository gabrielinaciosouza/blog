export class Author {
    authorId: string;
    name: string;
    email: string;
    profilePicture: string;

    constructor(authorId: string, name: string, email: string, profilePicture: string) {
        this.authorId = authorId;
        this.name = name;
        this.email = email;
        this.profilePicture = profilePicture;
    }
}