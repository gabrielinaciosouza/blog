import { Author } from './author';

export class Comment {
    commentId: string;
    content: string;
    creationDate: string;
    author: Author;

    constructor(commentId: string, content: string, creationDate: string, author: Author) {
        this.commentId = commentId;
        this.content = content;
        this.creationDate = creationDate;
        this.author = author;
    }
}