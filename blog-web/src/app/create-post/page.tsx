"use client"

import React, { useState, useEffect, useRef } from "react";
import styles from "./createPostPage.module.css";
import ReactQuill from "react-quill-new";
import "react-quill-new/dist/quill.snow.css";
import { useModal } from "@/hooks/useModal";
import { FaUpload, FaPlus, FaImage, FaEye } from "react-icons/fa";
import PostCard from "@/components/postCard/PostCard";
import Post from "@/models/post";
import Modal from "@/components/modal/Modal";
import Button from "@/components/button/Button";
import CreatePostRequest from "@/models/create-post-request";
import { createPost } from "@/services/postService";
import { useRouter } from "next/navigation";
import useStorage from "@/hooks/useStorage";
import useLoading from "@/hooks/useLoading";
import Loading from "@/components/loading/Loading";

export default function CreatePostPage() {
    const [open, setOpen] = useState(false);
    const [title, setTitle] = useStorage("draft-title", "");
    const [content, setContent] = useStorage("draft-content", "");
    const [coverImage, setCoverImage] = useStorage<string | null>("draft-coverImage", null);


    const { modalState, openModal, closeModal } = useModal();
    const { isLoading, startLoading, stopLoading } = useLoading();
    const router = useRouter();

    const titleRef = useRef<HTMLTextAreaElement>(null);
    const quillRef = useRef<ReactQuill>(null);
    const contentImageInputRef = useRef<HTMLInputElement>(null);
    const postImageInputRef = useRef<HTMLInputElement>(null);

    useEffect(() => {
        if (titleRef.current) {
            titleRef.current.style.height = "auto";
            titleRef.current.style.height = `${titleRef.current.scrollHeight}px`;
        }
    }, [title]);

    const handleImageUpload = async (e: React.ChangeEvent<HTMLInputElement>, type: string) => {
        let message = "Image not uploaded";
        try {
            if (e.target.files && e.target.files[0]) {
                const file = e.target.files[0];

                const formData = new FormData();
                formData.append("file", file);
                formData.append("fileName", file.name);
                formData.append("fileMimeType", file.type);

                const result = await fetch(`/api/images?type=${type}`, {
                    method: "POST",
                    body: formData,
                });
                if (!result.ok) {
                    throw new Error("Failed to upload image");
                }
                const data = await result.json();
                message = "Image uploaded successfully";
                return data.url;
            }
        } catch (error) {
            message = (error as Error).message;
            if (contentImageInputRef.current) {
                contentImageInputRef.current.value = "";
            }
        } finally {
            openModal(message, () => { });
        }
    };

    const handlePublish = async () => {
        if (!title.trim() || !content.trim()) {
            openModal("Title and content are required", () => { });
            return;
        }

        startLoading();

        try {
            const response = await fetch('/api/create-post', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ title, content, coverImage }),
            }).then(res => res.json());
            openModal("Post saved successfully", () => {
                setTitle("");
                setContent("");
                setCoverImage(null);
                router.push(`/posts/${response.slug}`);
            });
        } catch (error) {
            openModal((error as Error).message, () => { });
        } finally {
            stopLoading();
        }
    };

    const insertImageToEditor = (imageUrl: string | undefined) => {
        if (quillRef.current && imageUrl) {
            const editor = quillRef.current.getEditor();
            editor.focus();
            const range = editor.getSelection();
            if (range) {
                editor.insertEmbed(range.index, "image", imageUrl);
            }
        }
    };

    const handleImageInsert = () => {
        if (contentImageInputRef.current) {
            contentImageInputRef.current.click();
        }
    };

    const post: Post = {
        postId: "",
        title: title,
        content: content,
        coverImage: coverImage ?? "/logo2.png",
        creationDate: new Date().toISOString().replace('T', ' ').split('.')[0].slice(0, -3),
        slug: "",
    };

    return (
        <div className={styles.container}>
            {isLoading && <Loading />}
            <Modal isOpen={modalState.isOpen} content={modalState.content} onClose={closeModal} />
            <textarea
                ref={titleRef}
                className={styles.input}
                placeholder="Title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                rows={1}
            />
            <div className={styles.imageUpload}>
                <input
                    type="file"
                    id="postImage"
                    role="coverImageUpload"
                    ref={postImageInputRef}
                    className={styles.imageInput}
                    accept="image/*"
                    onChange={async (e) => {
                        const url = await handleImageUpload(e, "cover-images");
                        if (url) {
                            setCoverImage(url);
                        }
                    }}
                />
            </div>
            <div className={styles.editor}>
                <div className={styles.editorButtons}>
                    <Button onClick={() => setOpen(!open)} className={styles.addButton} ariaLabel="Plus">
                        <FaPlus />
                    </Button>
                </div>
                {open && (
                    <div className={styles.add}>
                        <Button onClick={handleImageInsert} className={styles.addButton} ariaLabel="Image">
                            <FaImage />
                        </Button>
                        <Button onClick={() => openModal(<PostCard {...post} />, () => { })} className={styles.button} ariaLabel="Preview">
                            <FaEye />
                        </Button>
                        <Button className={styles.uploadButton} onClick={() => postImageInputRef.current?.click()} ariaLabel="Upload Image">
                            <FaUpload />
                        </Button>
                    </div>
                )}
                <ReactQuill
                    ref={quillRef}
                    className={styles.textArea}
                    value={content}
                    onChange={setContent}
                    placeholder="Write your story..."
                />
            </div>
            <input
                type="file"
                ref={contentImageInputRef}
                className={styles.imageInput}
                accept="image/*"
                role="contentImageUpload"
                onChange={async (e) => {
                    const url = await handleImageUpload(e, "content-images");
                    if (url) {
                        insertImageToEditor(url);
                    }
                }}
            />
            <Button
                onClick={handlePublish}
                className={styles.publishButton}
                disabled={isLoading}
            >
                Publish
            </Button>
        </div>
    );
};