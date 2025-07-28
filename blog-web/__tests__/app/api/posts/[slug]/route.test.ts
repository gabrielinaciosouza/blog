import { DELETE } from '@/app/api/posts/[slug]/route';
import AuthResponse from '@/models/auth-response';
import { validateAuthResponse } from '@/services/authService';
import { deletePost } from '@/services/postService';
import { NextRequest, NextResponse } from 'next/server';

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