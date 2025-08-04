import AuthResponse from "@/models/auth-response";
import CreatePostRequest from "@/models/create-post-request";
import Post from "@/models/post";
import { getIdTokenByCustomToken } from "./firebase";

export const SERVER_URL = process.env.BLOG_API_URL || "http://127.0.0.1:8080";
export const POSTS_PATH = `${SERVER_URL}/posts`;

export const createPost = async (authResponse: AuthResponse, request: CreatePostRequest): Promise<Post> => {
    const idToken = await getIdTokenByCustomToken(authResponse.authToken);

    const response = await fetch(`${POSTS_PATH}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${idToken}`
        },
        body: JSON.stringify(request),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message);
    }

    return new Post(data.postId, data.title, data.content, data.creationDate, data.slug, data.coverImage);
};

export const getPostBySlug = async (slug: string): Promise<Post> => {
    const response = await fetch(`${POSTS_PATH}/${slug}`, {
        cache: "force-cache",
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message);
    }



    return new Post(data.postId, data.title, data.content, data.creationDate, data.slug, data.coverImage);
};

export const getPosts = async (page: number, size: number, headers: Record<string, string> | undefined): Promise<{ posts: Post[], totalCount: number }> => {
    const response = await fetch(`${POSTS_PATH}/find`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            ...headers
        },
        body: JSON.stringify({ page, size }),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message);
    }

    return data;
}

export const getDeletedPosts = async (auth: AuthResponse): Promise<Post[]> => {
    const idToken = await getIdTokenByCustomToken(auth.authToken);
    const response = await fetch(`${POSTS_PATH}/deleted/`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${idToken}`
        }
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message);
    }

    return data;
}

export const deletePost = async (authResponse: AuthResponse, slug: string): Promise<void> => {
    const idToken = await getIdTokenByCustomToken(authResponse.authToken);

    const response = await fetch(`${POSTS_PATH}/${slug}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${idToken}`
        }
    });

    if (!response.ok) {
        throw new Error("Failed to delete post");
    }
}