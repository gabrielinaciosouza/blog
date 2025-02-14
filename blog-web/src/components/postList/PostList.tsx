import React from "react";
import styles from "./postList.module.css"
import PostCard from "../postCard/PostCard";
import Link from "next/link";
import {getPosts} from "@/services/postService";

const PostList = async () => {
    try {
        const posts = await getPosts(1, 10);
        return (
            <div className={styles.container}>
                {
                    posts.length > 6 && (
                        <div className={styles.headerContainer}>
                            <div className={styles.headerText}>Recent Stories</div>
                            <Link className={styles.button} href="/posts">See more</Link>
                        </div>
                    )
                }
                <div className={styles.grid}>
                    {posts.slice(0, 6).map((post) => (
                        <div key={post.postId}>
                            <PostCard {...post} />
                        </div>
                    ))}

                </div>

            </div>
        )
    } catch {
        return <></>
    }

}

export default PostList;
