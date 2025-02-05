"use client"

import React, { useState } from "react";
import styles from "./createPostPage.module.css";
import Image from "next/image";
import ReactQuill from "react-quill-new";
import "react-quill-new/dist/quill.bubble.css";

const CreatePostPage = () => {
    const [open, setOpen] = useState(false);
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [loading, setLoading] = useState(false);

    const handlePublish = async () => {
        if (!title.trim() || !content.trim()) {
            alert("Title and content cannot be empty");
            return;
        }
        setLoading(true);
        try {
            const response = await fetch("http://localhost:8080/posts", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ title, content }),
            });
            if (!response.ok) {
                throw new Error("Failed to publish post");
            }
            alert("Post published successfully!");
            const responseBody = await response.json();
            console.log("response " + JSON.stringify(responseBody));
        } catch (error) {
            console.log(error);
        
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.container}>
            <input 
                className={styles.input} 
                type="text" 
                placeholder="Title" 
                value={title} 
                onChange={(e) => setTitle(e.target.value)}
            />
            <div className={styles.editor}>
                <button className={styles.button} onClick={() => setOpen(!open)}>
                    <Image src="/plus.png" alt="" width={16} height={16} />
                </button>
                {open && (
                    <div className={styles.add}>
                        <button className={styles.addButton}>
                            <Image src="/image.png" alt="" width={16} height={16} />
                        </button>
                        <button className={styles.addButton}>
                            <Image src="/external.png" alt="" width={16} height={16} />
                        </button>
                        <button className={styles.addButton}>
                            <Image src="/video.png" alt="" width={16} height={16} />
                        </button>
                    </div>
                )}
                <ReactQuill 
                    className={styles.textArea} 
                    theme="bubble" 
                    value={content} 
                    onChange={setContent} 
                    placeholder="Write your story..."
                />
            </div>
            <button 
                className={styles.publish} 
                onClick={handlePublish} 
                disabled={loading}
            >
                {loading ? "Publishing..." : "Publish"}
            </button>
        </div>
    );
};

export default CreatePostPage;
