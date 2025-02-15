import CreatePostRequest from "@/models/create-post-request";
import Post from "@/models/post";

export const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://127.0.0.1:3000";
export const SERVER_URL = process.env.NEXT_PUBLIC_API_URL || "http://127.0.0.1:8080";
const POSTS_PATH  = `${SERVER_URL}/posts`;

export const createPost = async (request: CreatePostRequest): Promise<Post> => {
    const response = await fetch(`${POSTS_PATH}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(request),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message);
    }

    return new Post(data.postId, data.title, data.content, data.creationDate, data.slug);
};

export const getPostBySlug = async (slug: string): Promise<Post> => {
    const response = await fetch(`${POSTS_PATH}/${slug}`, {
        cache: "force-cache",
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message);
    }

    

    return new Post(data.postId, data.title, data.content, data.creationDate, data.slug);
};

export const getPosts = async (page: number, size: number): Promise<Post[]> => {
    const response = await fetch(`${POSTS_PATH}/find`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ page, size }),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message);
    }

    return data.map((post: any) => new Post(post.postId, post.title, post.content, post.creationDate, post.slug));
}
