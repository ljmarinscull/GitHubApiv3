package com.example.githubapiv3.ui.repository_webview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.githubapiv3.databinding.FragmentRepositoryWebviewBinding

class RepositoryWebViewFragment : Fragment() {

    private var _binding: FragmentRepositoryWebviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepositoryWebviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            webView.loadUrl(navArgs<RepositoryWebViewFragmentArgs>().value.url)
            webView.webViewClient = object : WebViewClient() {
                override fun onReceivedError(
                    view: WebView?, request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    progressBar.visibility = GONE
                }

                override fun onPageFinished(view: WebView, url: String) {
                    progressBar.visibility = GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}