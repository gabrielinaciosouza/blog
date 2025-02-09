class CreatePostRequest {
    title: string;
    content: string;

    constructor(title: string, content: string) {
        this.title = title;
        this.content = content;
    }
}

export default CreatePostRequest;