import React from "react";
import styles from "./singlePost.module.css"
import Image from "next/image";
import { getPostBySlug } from "@/services/postService";

interface SinglePageProps {
    params: {
        slug: string;
    };
}

const SinglePage = async ({ params }: SinglePageProps) => {

    const { slug } = await params;

    try {
    const post = await getPostBySlug(slug);
    return (
        <div className={styles.container}>
            <div className={styles.infoContainer}>
                <div className={styles.textContainer}>
                    <h1 className={styles.title}>{post.title}</h1>
                    <div className={styles.user}>
                        <div className={styles.userImageContainer}>
                            <Image src="/profile-picture.png" alt="" fill className={styles.avatar} />
                        </div>


                        <div className={styles.userTextContainer}>
                            <span className={styles.userName}>Gabriel Inacio</span>
                            <span className={styles.date}>{post.creationDate}</span>
                        </div>
                    </div>
                </div>

            </div>
            <div className={styles.content}>
                <div className={styles.post}>
                    <div dangerouslySetInnerHTML={{ __html: post.content }} />
                </div>
            </div>
        </div>
    )
    } catch (err) {
        const error = err instanceof Error ? err.message : "Unknown error";
        return (
            <div className={styles.errorContainer}>
                <h1>Post Not Found</h1>
                <p>{error}</p>
            </div>
        );
    }
   
}

export default SinglePage;