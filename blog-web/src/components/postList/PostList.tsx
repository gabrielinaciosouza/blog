import React from "react";
import styles from "./postList.module.css"
import PostCard from "../postCard/PostCard";
import { getPosts } from "@/services/postService";

const PostList = async () => {
    try {
        const posts = await getPosts(1, 7);
        return (
            <div className={styles.container}>
                <div className={styles.grid}>
                    {posts.slice(0, 6).map((post) => (
                        <div key={post.postId}>
                            <PostCard {...post} />
                        </div>
                    ))}

                </div>
                {
                    posts.length > 6 && (
                        <div className={styles.buttonContainer}>       
                            <button className={styles.button}>See more stories</button>
                        </div>
                    )
                }
            </div>
        )
    }
    catch (error) {
        console.error(error);
    }
    return <></>
}

export default PostList;