class CreatePostRequest {
    title: string;
    content: string;
    coverImage: string | null;

    constructor(title: string, content: string, coverImage: string | null) {
        this.title = title;
        this.content = content;
        this.coverImage = coverImage;
    }
}

export default CreatePostRequest;