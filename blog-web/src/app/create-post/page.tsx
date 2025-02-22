"use client"

import React, { useState, useEffect, useRef } from "react";
import styles from "./createPostPage.module.css";
import ReactQuill from "react-quill-new";
import "react-quill-new/dist/quill.snow.css";
import { useModal } from "@/hooks/useModal";
import { FaUpload, FaPlus, FaImage, FaExternalLinkAlt, FaVideo, FaEye } from "react-icons/fa";
import BlogImage from "@/models/blog-image";
import PostCard from "@/components/postCard/PostCard";
import Post from "@/models/post";
import Modal from "@/components/modal/Modal";
import Button from "@/components/button/Button";
import CreatePostRequest from "@/models/create-post-request";
import { createPost } from "@/services/postService";
import { useRouter } from "next/navigation";

export default function CreatePostPage() {
    const [open, setOpen] = useState(false);
    const [uploadedImages, setUploadedImages] = useState<Map<string, string>>(new Map());
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [coverImage, setCoverImage] = useState<string | null>(null);


    useEffect(() => {
        const savedTitle = localStorage.getItem("draft-title");
        const savedContent = localStorage.getItem("draft-content");
        const savedCoverImage = localStorage.getItem("draft-coverImage");
        if (savedTitle) setTitle(savedTitle);
        if (savedContent) setContent(savedContent);
        if (savedCoverImage) setCoverImage(savedCoverImage);
    }, []);

    useEffect(() => {
        localStorage.setItem("draft-title", title);
        localStorage.setItem("draft-content", content);
        if (coverImage) {
            localStorage.setItem("draft-coverImage", coverImage);
        } else {
            localStorage.removeItem("draft-coverImage");
        }
    }, [title, content, coverImage]);

    const { modalState, openModal, closeModal } = useModal();
    const [loading, setLoading] = useState(false);
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

    const handleImageUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
        try {
            if (e.target.files && e.target.files[0]) {
                const formData = new FormData();
                formData.append("file", e.target.files[0]);
                formData.append("fileName", e.target.files[0].name);
                formData.append("fileMimeType", e.target.files[0].type);

                const result = await fetch(`/api/images?type=cover-images`, {
                    method: "POST",
                    body: formData,
                });
                if (!result.ok) {
                    throw new Error("Failed to upload cover image");
                }
                const data = await result.json();
                setCoverImage(data.url);
            }
        } catch (error) {
            openModal((error as Error).message, () => setCoverImage(null));
            if (postImageInputRef.current) {
                postImageInputRef.current.value = "";
            }
        }
    };

    const handleContentImageUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
        try {
            if (e.target.files && e.target.files[0]) {
                const file = e.target.files[0];
                const fileKey = `${file.name}-${file.size}-${file.lastModified}`;

                if (uploadedImages.has(fileKey)) {
                    const imageUrl = uploadedImages.get(fileKey);
                    insertImageToEditor(imageUrl);
                } else {
                    const formData = new FormData();
                    formData.append("file", file);
                    formData.append("fileName", file.name);
                    formData.append("fileMimeType", file.type);

                    const result = await fetch(`/api/images?type=content-images`, {
                        method: "POST",
                        body: formData,
                    });
                    if (!result.ok) {
                        throw new Error("Failed to upload content image");
                    }
                    const data = await result.json();
                    const imageUrl = data.url;
                    setUploadedImages(new Map(uploadedImages.set(fileKey, imageUrl)));
                    insertImageToEditor(imageUrl);
                }
            }
        } catch (error) {
            openModal((error as Error).message, () => { });
            if (contentImageInputRef.current) {
                contentImageInputRef.current.value = "";
            }
        }
    };

    const handlePublish = async () => {
        if (!title.trim() || !content.trim()) {
            openModal("Title and content are required", () => { });
            return;
        }

        setLoading(true);

        try {
            const response = await createPost(new CreatePostRequest(title, content, coverImage));
            openModal("Post saved successfully", () => {
                localStorage.removeItem("draft-title");
                localStorage.removeItem("draft-content");
                localStorage.removeItem("draft-coverImage");
                router.push(`/posts/${response.slug}`);
            });
        } catch (error) {
            openModal((error as Error).message, () => { });
        } finally {
            setLoading(false);
        }
    };

    const insertImageToEditor = (imageUrl: string | undefined) => {
        if (quillRef.current && imageUrl) {
            const editor = quillRef.current.getEditor();
            editor.focus();
            const range = editor.getSelection();
            if (range) {
                editor.insertEmbed(range.index, "image", imageUrl);
            } else {
                editor.insertEmbed(editor.getLength() - 1, "image", imageUrl);
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
        creationDate: new Date().toISOString().split('T')[0],
        slug: "",
    };

    return (
        <div className={styles.container}>
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
                    ref={postImageInputRef}
                    className={styles.imageInput}
                    accept="image/*"
                    onChange={handleImageUpload}
                />
            </div>
            <div className={styles.editor}>
                <div className={styles.editorButtons}>
                    <Button onClick={() => setOpen(!open)} className={styles.addButton}>
                        <FaPlus />
                    </Button>
                </div>
                {open && (
                    <div className={styles.add}>
                        <Button onClick={handleImageInsert} className={styles.addButton}>
                            <FaImage />
                        </Button>
                        <Button onClick={() => openModal(<PostCard {...post} />, () => { })} className={styles.button}>
                            <FaEye />
                        </Button>
                        <Button className={styles.uploadButton} onClick={() => postImageInputRef.current?.click()}>
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
                onChange={handleContentImageUpload}
                style={{ display: "none" }}
            />
            <Button
                onClick={handlePublish}
                className={styles.publishButton}
                disabled={loading}
            >
                {loading ? "Publishing..." : "Publish"}
            </Button>
        </div>
    );
};