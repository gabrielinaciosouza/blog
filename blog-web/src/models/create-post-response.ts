class CreatePostResponse {
    postId: string;
    title: string;
    content: string;
    creationDate: string;

    constructor(postId: string, title: string, content: string, creationDate: string) {
        this.postId = postId
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
    }
}

export default CreatePostResponse;