import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { createPost } from "@/services/postService";
import CreatePostRequest from "@/models/create-post-request";
import Post from "@/models/post";

export const usePublishPost = () => {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [coverImage, setCoverImage] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [responseMessage, setResponseMessage] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [postResponse, setPostResponse] = useState<Post | null>(null);
  const router = useRouter();

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

  const handlePublish = async () => {
    console.log("handlePublish");
    if (!title.trim() || !content.trim()) {
      setResponseMessage("Title and/or content cannot be empty");
      setShowModal(true);
      return;
    }

    setLoading(true);
    setResponseMessage("");

    try {
      console.log("title", title);
      console.log("content", content);
      console.log("coverImage", coverImage);
      const response = await createPost(new CreatePostRequest(title, content, coverImage));
      setResponseMessage("Post saved successfully!");
      setPostResponse(response);
      setShowModal(true);
    } catch (error) {
      setResponseMessage((error as Error).message);
      setShowModal(true);
    } finally {
      setLoading(false);
    }
  };

  const handleCloseModal = () => {
    setShowModal(false);
    if (postResponse) {
      localStorage.removeItem("draft-title");
      localStorage.removeItem("draft-content");
      localStorage.removeItem("draft-coverImage");
      router.push(`/posts/${postResponse.slug}`);
    }
  };

  return {
    title,
    setTitle,
    content,
    setContent,
    coverImage,
    setCoverImage,
    loading,
    responseMessage,
    showModal,
    handlePublish,
    handleCloseModal,
  };
};