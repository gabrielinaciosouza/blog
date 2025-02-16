import React from "react";
import styles from "./postList.module.css"
import PostCard from "../postCard/PostCard";
import Link from "next/link";
import { getPosts } from "@/services/postService";

const PostList = async () => {
    try {
        const { posts, totalCount } = await getPosts(1, 10);
        return (
            <div className={styles.container}>


                <div className={styles.headerContainer}>
                    {
                        totalCount > 0 && (<div className={styles.headerText}>Recent Stories</div>)
                    }
                    {
                        totalCount > 6 && (<Link className={styles.button} href="/posts">See more</Link>)
                    }

                </div>


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
