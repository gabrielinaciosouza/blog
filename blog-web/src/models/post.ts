class Post {
    postId: string;
    title: string;
    content: string;
    creationDate: string;
    slug: string;

    constructor(postId: string, title: string, content: string, creationDate: string, slug: string) {
        this.postId = postId
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.slug = slug;
    }
}

export default Post;