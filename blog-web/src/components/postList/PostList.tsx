import React from "react";
import styles from "./postList.module.css"
import PostCard from "../postCard/PostCard";
import { getPosts } from "@/services/postService";

const PostList = async () => {
    const posts = await getPosts(1, 10);
    return (
         <div className={styles.container}>
            {posts.map((post) => (
                <div key={post.postId}>
                    <PostCard {...post} />
                </div>
            ))}
                  
         </div>
    )
}

export default PostList;
