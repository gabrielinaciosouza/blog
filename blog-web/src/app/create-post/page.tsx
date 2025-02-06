"use client"

import React, { useState } from "react";
import { useRouter } from "next/navigation";
import styles from "./createPostPage.module.css";
import Image from "next/image";
import ReactQuill from "react-quill-new";
import "react-quill-new/dist/quill.bubble.css";

const CreatePostPage = () => {
    const [open, setOpen] = useState(false);
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [loading, setLoading] = useState(false);
    const [responseMessage, setResponseMessage] = useState("");
    const [showModal, setShowModal] = useState(false);
    const [postId, setPostId] = useState(null);
    const router = useRouter();

    const handlePublish = async () => {
        if (!title.trim() || !content.trim()) {
            setResponseMessage("Title and content cannot be empty");
            setShowModal(true);
            return;
        }
        setLoading(true);
        setResponseMessage("");
        try {
            const response = await fetch("http://localhost:8080/posts", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ title, content }),
            });
            const data = await response.json();
            if (!response.ok) {
                throw new Error(data.message || "Failed to publish post");
            }
            setResponseMessage("Post saved successfully!");
            setPostId(data.postId);
            setShowModal(true);
        } catch (error) {
            if (error instanceof Error) {
                setResponseMessage(`Error: ${error.message}`);
            } else {
                setResponseMessage("Unknown error");
            }
            setShowModal(true);
        } finally {
            setLoading(false);
        }
    };

    const handleCloseModal = () => {
        setShowModal(false);
        if (postId) {
            router.push(`/posts/${postId}`);
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
            {showModal && (
                <div className={styles.modal}>
                    <div className={styles.modalContent}>
                        <p>{responseMessage}</p>
                        <button className={styles.closeButton} onClick={handleCloseModal}>OK</button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CreatePostPage;
