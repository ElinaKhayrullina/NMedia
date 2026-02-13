package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.databinding.FragmentImageViewBinding
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.view.load

class ImageViewFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentImageViewBinding.inflate(inflater, container, false)

        val imageUrl = arguments?.textArg
        if (imageUrl != null) {
            binding.imageView.load("${BuildConfig.BASE_URL}/media/$imageUrl")
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}