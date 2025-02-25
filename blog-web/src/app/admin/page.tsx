"use client"

import React, { useState } from 'react';
import styles from './admin.module.css';
import PostTile from '@/components/postTile/PostTile';
import Post from '@/models/post';
import { useRouter } from 'next/navigation';
import Button from '@/components/button/Button';

const AdminPage = () => {
    const router = useRouter();
    const [posts, setPosts] = useState<Post[]>([
        new Post("1", 'Post 1', 'post-1', '2021-09-01', 'This is the content of post 1', 'coverImage.jpg'),
        new Post("2", 'Post 2', 'post-2', '2021-09-02', 'This is the content of post 2', 'coverImage.jpg'),
    ]);

    const [deletedPosts, setDeletedPosts] = useState<Post[]>([
        new Post("3", 'Post 3', 'post-3', '2021-09-03', 'This is the content of post 3', 'coverImage.jpg'),
        new Post("4", 'Post 4', 'post-4', '2021-09-04', 'This is the content of post 4', 'coverImage.jpg'),
    ]);

    const handleEdit = (id: string) => {
        // Handle edit logic here
        console.log(`Edit post with id: ${id}`);
    };

    const handleDelete = (id: string) => {
        // Handle delete logic here
        console.log(`Delete post with id: ${id}`);
        const postToDelete = posts.find(post => post.postId === id);
        setPosts(posts.filter(post => post.postId !== id));
        if (postToDelete) {
            setDeletedPosts([...deletedPosts, postToDelete]);
        }
    };

    const handleRestore = (id: string) => {
        // Handle restore logic here
        console.log(`Restore post with id: ${id}`);
        const postToRestore = deletedPosts.find(post => post.postId === id);
        setDeletedPosts(deletedPosts.filter(post => post.postId !== id));
        if (postToRestore) {
            setPosts([...posts, postToRestore]);
        }
    };

    const handleCreateNewPost = () => {
        router.push('/create-post');
    };

    return (
        <div className={styles.adminContainer}>
            <div className={styles.buttonContainer}>
                <Button className={styles.createButton} onClick={handleCreateNewPost}>Create New Post</Button>
            </div>
            <div className={styles.deletedPostsHeader}>Deleted Posts</div>
            {deletedPosts.map(post => (
                <PostTile
                    key={post.postId}
                    post={post}
                    onEdit={handleEdit}
                    onRestore={handleRestore}
                    onOpen={() => router.push(`/post/${post.slug}`)}
                />
            ))}
            <div className={styles.activePosts}>Active Posts</div>
            {posts.map(post => (
                <PostTile
                    key={post.postId}
                    post={post}
                    onEdit={handleEdit}
                    onDelete={handleDelete}
                    onOpen={() => router.push(`/post/${post.slug}`)}
                />
            ))}
        </div>
    );
};

export default AdminPage;