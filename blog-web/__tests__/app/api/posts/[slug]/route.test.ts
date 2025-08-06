jest.mock('next/cache', () => ({
    revalidatePath: jest.fn(),
}));
import { DELETE, GET } from '@/app/api/posts/[slug]/route';
import AuthResponse from '@/models/auth-response';
import { validateAuthResponse } from '@/services/authService';
import { deletePost, getPostBySlug } from '@/services/postService';
import { NextRequest, NextResponse } from 'next/server';

import { revalidatePath } from 'next/cache';

jest.mock('@/services/postService');
jest.mock('@/services/authService');
jest.mock('@/services/firebase', () => ({
    getIdTokenByCustomToken: jest.fn().mockResolvedValue('fake-token'),
}));

jest.mock('next/server', () => ({
    ...jest.requireActual('next/server'),
    NextRequest: jest.fn().mockImplementation((url: string) => ({
        url,
        nextUrl: {
            pathname: new URL(url).pathname,
            search: new URL(url).search,
            clone: jest.fn().mockReturnValue({
                pathname: new URL(url).pathname,
                search: new URL(url).search,
            }),
        },
        headers: {
            get: jest.fn().mockImplementation(key => {
                if (key === 'cookie') {
                    return "authResponse=" + JSON.stringify(new AuthResponse(
                        'validAuthToken',
                        'userId123',
                        'ADMIN',
                        'John Doe',
                        'email@example.com',
                        'http://example.com/picture.jpg'
                    ));
                }
            }),
        },
        json: jest.fn().mockResolvedValue({
            posts: [],
            count: 0,
        }),
    })),
}));

describe('DELETE /api/posts/[slug]', () => {
    beforeEach(() => {
        jest.spyOn(NextResponse, 'json').mockImplementation((body) => {
            return {
                json: jest.fn().mockResolvedValue(body)
            } as any;
        });
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('should return 401 if cookie is missing', async () => {
        const req = new NextRequest('http://localhost/api/posts/test-post');
        (req.headers.get as jest.Mock).mockImplementation(() => null);

        const response = await DELETE(req, { params: Promise.resolve({ slug: 'test-post' }) });
        const jsonResponse = await response.json();
        expect(jsonResponse).toEqual({ message: 'Unauthorized' });
    });

    it('should return 401 if authResponse header is missing', async () => {
        const req = new NextRequest('http://localhost/api/posts/test-post');
        (req.headers.get as jest.Mock).mockImplementation(key => {
            if (key === 'cookie') {
                return "any=" + JSON.stringify({ some: 'value' });
            }
        });

        const response = await DELETE(req, { params: Promise.resolve({ slug: 'test-post' }) });
        const jsonResponse = await response.json();
        expect(jsonResponse).toEqual({ message: 'Unauthorized' });
    });

    it('should delete the post and return a success message', async () => {
        const slug = 'test-post';
        const req = new NextRequest('http://localhost/api/posts/test-post');
        const params = { slug };

        (deletePost as jest.Mock).mockResolvedValueOnce(undefined);
        (validateAuthResponse as jest.Mock).mockReturnValueOnce(new AuthResponse(
            'validAuthToken',
            'userId123',
            'ADMIN',
            'John Doe',
            'email@example.com',
            'http://example.com/picture.jpg'
        ));


        const response = await DELETE(req, { params: Promise.resolve(params) });

        expect(deletePost).toHaveBeenCalledWith(expect.any(AuthResponse), slug);
        const jsonResponse = await response.json();
        expect(jsonResponse).toEqual({ message: 'Post deleted successfully' });
    });

    it('should return an error message if something goes wrong', async () => {
        const slug = 'test-post';
        const req = new NextRequest('http://localhost/api/posts/test-post');
        const params = { slug };

        (deletePost as jest.Mock).mockRejectedValueOnce(new Error('Failed to delete post'));
        (validateAuthResponse as jest.Mock).mockReturnValueOnce(new AuthResponse(
            'validAuthToken',
            'userId123',
            'ADMIN',
            'John Doe',
            'email@example.com',
            'http://example.com/picture.jpg'
        ));

        const response = await DELETE(req, { params: Promise.resolve(params) });

        expect(deletePost).toHaveBeenCalledWith(expect.any(AuthResponse), slug);
        const jsonResponse = await response.json();
        expect(jsonResponse).toEqual({ message: 'Something went wrong!' });
    });
});

describe('GET /api/posts/[slug]', () => {
    beforeEach(() => {
        jest.spyOn(NextResponse, 'json').mockImplementation((body, init) => {
            return {
                json: jest.fn().mockResolvedValue(body),
                status: init?.status,
                body,
            } as any;
        });
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('should return the post if found', async () => {
        const slug = 'test-slug';
        const req = new NextRequest('http://localhost/api/posts/test-slug');
        const params = { slug };
        const mockPost = { postId: '1', title: 'Test', content: 'Content', creationDate: '2025-01-01', slug, coverImage: 'img.jpg' };
        (getPostBySlug as jest.Mock).mockResolvedValueOnce(mockPost);
        const response = await GET(req, { params: Promise.resolve(params) });
        expect(getPostBySlug).toHaveBeenCalledWith(slug);
        const jsonResponse = await response.json();
        expect(jsonResponse).toEqual(mockPost);
        expect(response.status).toBeUndefined(); // 200 is default
    });

    it('should return 404 if post is not found', async () => {
        const slug = 'not-found';
        const req = new NextRequest('http://localhost/api/posts/not-found');
        const params = { slug };
        (getPostBySlug as jest.Mock).mockResolvedValueOnce(null);
        const response = await GET(req, { params: Promise.resolve(params) });
        const jsonResponse = await response.json();
        expect(jsonResponse).toEqual({ error: 'Post not found' });
        expect(response.status).toBe(404);
    });

    it('should return 500 if an error occurs', async () => {
        const slug = 'error-slug';
        const req = new NextRequest('http://localhost/api/posts/error-slug');
        const params = { slug };
        (getPostBySlug as jest.Mock).mockRejectedValueOnce(new Error('DB error'));
        const response = await GET(req, { params: Promise.resolve(params) });
        const jsonResponse = await response.json();
        expect(jsonResponse).toEqual({ error: 'DB error' });
        expect(response.status).toBe(500);
    });
});

describe('PUT /api/posts/[slug]', () => {
    beforeEach(() => {
        jest.spyOn(NextResponse, 'json').mockImplementation((body, init) => {
            return {
                json: jest.fn().mockResolvedValue(body),
                status: init?.status,
                body,
            } as any;
        });
    });
    afterEach(() => {
        jest.clearAllMocks();
    });

    it('should return 401 if cookie is missing', async () => {
        const req = new NextRequest('http://localhost/api/posts/test-post');
        (req.headers.get as jest.Mock).mockImplementation(() => null);
        const response = await (await import('@/app/api/posts/[slug]/route')).PUT(req, { params: Promise.resolve({ slug: 'test-post' }) });
        const jsonResponse = await response.json();
        expect(jsonResponse).toEqual({ message: 'Unauthorized' });
        expect(response.status).toBe(401);
    });

    it('should return 401 if authResponse header is missing', async () => {
        const req = new NextRequest('http://localhost/api/posts/test-post');
        (req.headers.get as jest.Mock).mockImplementation(key => {
            if (key === 'cookie') {
                return "any=" + JSON.stringify({ some: 'value' });
            }
        });
        const response = await (await import('@/app/api/posts/[slug]/route')).PUT(req, { params: Promise.resolve({ slug: 'test-post' }) });
        const jsonResponse = await response.json();
        expect(jsonResponse).toEqual({ message: 'Unauthorized' });
        expect(response.status).toBe(401);
    });

    it('should edit the post and return 204', async () => {
        const slug = 'test-post';
        const req = new NextRequest('http://localhost/api/posts/test-post');
        const params = { slug };
        (req.headers.get as jest.Mock).mockImplementation(key => {
            if (key === 'cookie') {
                return "authResponse=" + JSON.stringify(new AuthResponse(
                    'validAuthToken',
                    'userId123',
                    'ADMIN',
                    'John Doe',
                    'email@example.com',
                    'http://example.com/picture.jpg'
                ));
            }
        });
        req.json = jest.fn().mockResolvedValue({ title: 'Updated', content: 'Updated', coverImage: 'img.jpg' });
        const editPost = require('@/services/postService').editPost;
        editPost.mockResolvedValueOnce(undefined);
        const validateAuthResponse = require('@/services/authService').validateAuthResponse;
        validateAuthResponse.mockReturnValueOnce(new AuthResponse(
            'validAuthToken',
            'userId123',
            'ADMIN',
            'John Doe',
            'email@example.com',
            'http://example.com/picture.jpg'
        ));
        const revalidatePathMock = require('next/cache').revalidatePath;
        revalidatePathMock.mockClear();
        const response = await (await import('@/app/api/posts/[slug]/route')).PUT(req, { params: Promise.resolve(params) });
        expect(editPost).toHaveBeenCalledWith(expect.any(AuthResponse), slug, { title: 'Updated', content: 'Updated', coverImage: 'img.jpg' });
        expect(revalidatePathMock).toHaveBeenCalledWith(`/posts/${slug}`);
        expect(revalidatePathMock).toHaveBeenCalledWith(`/posts`);
        const jsonResponse = await response.json();
        expect(jsonResponse).toEqual({ status: 204 });
        expect(response.status).toBeUndefined();
    });

    it('should return an error message if something goes wrong', async () => {
        const slug = 'test-post';
        const req = new NextRequest('http://localhost/api/posts/test-post');
        const params = { slug };
        (req.headers.get as jest.Mock).mockImplementation(key => {
            if (key === 'cookie') {
                return "authResponse=" + JSON.stringify(new AuthResponse(
                    'validAuthToken',
                    'userId123',
                    'ADMIN',
                    'John Doe',
                    'email@example.com',
                    'http://example.com/picture.jpg'
                ));
            }
        });
        req.json = jest.fn().mockResolvedValue({ title: 'Updated', content: 'Updated', coverImage: 'img.jpg' });
        const editPost = require('@/services/postService').editPost;
        editPost.mockRejectedValueOnce(new Error('Failed to edit post'));
        const validateAuthResponse = require('@/services/authService').validateAuthResponse;
        validateAuthResponse.mockReturnValueOnce(new AuthResponse(
            'validAuthToken',
            'userId123',
            'ADMIN',
            'John Doe',
            'email@example.com',
            'http://example.com/picture.jpg'
        ));
        const response = await (await import('@/app/api/posts/[slug]/route')).PUT(req, { params: Promise.resolve(params) });
        expect(editPost).toHaveBeenCalledWith(expect.any(AuthResponse), slug, { title: 'Updated', content: 'Updated', coverImage: 'img.jpg' });
        const jsonResponse = await response.json();
        expect(jsonResponse).toEqual({ message: 'Something went wrong!' });
        expect(response.status).toBe(500);
    });
});