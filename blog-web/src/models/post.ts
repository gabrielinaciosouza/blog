class Post {
    postId: string;
    title: string;
    content: string;
    creationDate: string;
    slug: string;
    coverImage: string;

    constructor(postId: string, title: string, content: string, creationDate: string, slug: string, coverImage: string) {
        this.postId = postId
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.slug = slug;
        this.coverImage = coverImage;
    }
}

export default Post;