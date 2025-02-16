import React from "react";
import styles from "./posts.module.css"
import PostCard from "@/components/postCard/PostCard"
import Post from "@/models/post";
import Pagination from "@/components/pagination/Pagination";
import { API_URL } from "@/services/postService";

const POST_PER_PAGE = 9;
const getData = async (page: number): Promise<{ posts: Post[], count: number }> => {
    const response = await fetch(
        `${API_URL}/api/posts?page=${page}&size=${POST_PER_PAGE}`,
        {
            cache: "no-store",
        }
    );

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message);
    }

    return data;
};

const Posts = async ({ searchParams } : { searchParams: { page: number | null }}) => {
    try {
        const page = (await searchParams).page || 1;
        const { posts, count } = await getData(page);
        const hasPrev = page > 1;
        const hasNext = count == POST_PER_PAGE;

        return (
            <div className={styles.container}>
                {
                    posts.length > POST_PER_PAGE && (
                        <div className={styles.headerContainer}>
                            <div className={styles.headerText}>Recent Stories</div>
                        </div>
                    )
                }
                <div className={styles.grid}>
                    {posts.map((post) => (
                        <div key={post.postId}>
                            <PostCard {...post} />
                        </div>
                    ))}

                </div>
                <div>
                    <Pagination page={page} hasNext={hasNext} hasPrev={hasPrev} />
                </div>
            </div>
        )
    } catch(err) {
        console.log(err);
        return <></>
    }

}

export default Posts;
