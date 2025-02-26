"use client"

import React, { useState, useEffect } from 'react';
import styles from './admin.module.css';
import PostTile from '@/components/postTile/PostTile';
import Post from '@/models/post';
import { useRouter, useSearchParams } from 'next/navigation';
import Button from '@/components/button/Button';
import useLoading from '@/hooks/useLoading';
import Loading from '@/components/loading/Loading';
import Pagination from '@/components/pagination/Pagination';
import Tabs from '@/components/tabs/Tabs';

const AdminPage = () => {
    const router = useRouter();
    const searchParams = useSearchParams();
    const { isLoading, startLoading, stopLoading } = useLoading();
    const [posts, setPosts] = useState<Post[]>([]);
    const [deletedPosts, setDeletedPosts] = useState<Post[]>([]);
    const [totalCount, setTotalCount] = useState(0);
    const page = parseInt(searchParams.get('page') || '1');
    const pageSize = 10;

    const fetchData = async () => {
        startLoading();
        try {
            const [deletedResponse, postsResponse] = await Promise.all([
                fetch('/api/posts/deleted'),
                fetch(`/api/posts?page=${page}&size=${pageSize}`)
            ]);

            let deletedData = [];
            let availablePosts = [];
            let totalCountResult = 0;
            if (deletedResponse.ok) {
                deletedData = await deletedResponse.json();
            }

            if (postsResponse.ok) {
                const { posts, totalCount } = await postsResponse.json();
                if (posts.length === 0 && page > 1) {
                    router.push('/admin?page=1');
                    return;
                }
                availablePosts = posts;
                totalCountResult = totalCount;
            }

            setDeletedPosts(deletedData);
            setPosts(availablePosts);
            setTotalCount(totalCountResult);
        } catch (error) {
            console.error('Error fetching data:', error);
        } finally {
            stopLoading();
        }
    };

    useEffect(() => {
       
        fetchData();
    },
     // eslint-disable-next-line react-hooks/exhaustive-deps
    [page, startLoading, stopLoading]);

    const handleDelete = async (slug: string) => {
        try {
            startLoading();
            const response = await fetch(`/api/posts/${slug}`, {
                method: 'DELETE',
            });

            if (response.ok) {

                fetchData();

            } else {
                console.error('Failed to delete the post');
            }
        } catch (error) {
            console.error('Error deleting post:', error);
        } finally {
            stopLoading();
        }
    };

    const handleCreateNewPost = () => {
        router.push('/create-post');
    };

    const totalPages = Math.ceil(totalCount / pageSize);
    const hasPrev = page > 1;
    const hasNext = page < totalPages;

    const tabs = [
        {
            label: 'Active Posts',
            content: (
                <>
                    {posts.length > 0 ? (
                        <>
                            {posts.map(post => (
                                <PostTile
                                    key={post.postId}
                                    post={post}
                                    onDelete={() => handleDelete(post.slug)}
                                />
                            ))}
                            {totalCount > pageSize &&
                                <Pagination page={page} hasPrev={hasPrev} hasNext={hasNext} />}
                        </>
                    ) : (
                        <div>No posts available</div>
                    )}
                </>
            )
        },
        {
            label: 'Deleted Posts',
            content: (
                <>
                    {deletedPosts.length > 0 ? (
                        <>
                            {deletedPosts.map(post => (
                                <PostTile
                                    key={post.postId}
                                    post={post}
                                />
                            ))}
                        </>
                    ) : (
                        <div>No deleted posts</div>
                    )}
                </>
            )
        }
    ];

    return (
        <div className={styles.adminContainer}>
            <div className={styles.header}>
                <div className={styles.title}>Dashboard</div>
                <Button onClick={handleCreateNewPost}>New Post</Button>
            </div>
            {isLoading && (<Loading />)}
            <Tabs tabs={tabs} />
        </div>
    );
};

export default AdminPage;